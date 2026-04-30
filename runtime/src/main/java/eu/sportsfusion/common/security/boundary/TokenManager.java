package eu.sportsfusion.common.security.boundary;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import eu.sportsfusion.common.security.entity.UserDetails;

/**
 * @author Anatoli Pranovich
 */
@ApplicationScoped
public class TokenManager {
    @ConfigProperty(name = "sf.jwt.key")
    String jwtKey;

    private JwtParser jwtParser;
    private SecretKey jwtSecretKey;

    @PostConstruct
    void init() {
        jwtSecretKey = new SecretKeySpec(decodeJwtKey(jwtKey), "HmacSHA512");
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build();
    }

    private static byte[] decodeJwtKey(String jwtKey) {
        if (jwtKey == null) {
            return new byte[0];
        }

        try {
            return Decoders.BASE64.decode(jwtKey);
        } catch (IllegalArgumentException ignored) {
            // Backwards-compatible fallback: treat as raw string secret
            return jwtKey.getBytes(StandardCharsets.UTF_8);
        }
    }


    public String createToken(UserDetails userDetails) {
        Instant now = Instant.now();
        return createToken(userDetails, Date.from(now.plus(30, ChronoUnit.MINUTES)));
    }

    public String createToken(UserDetails userDetails, Date expiresAt) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder().
                setIssuedAt(Date.from(now)).
                setExpiration(expiresAt).
                setSubject(userDetails.getId().toString()).
                claim("firstName", userDetails.getFirstName()).
                claim("lastName", userDetails.getLastName()).
                claim("roles", userDetails.getRoles());

        if (userDetails.getAvatarUrl() != null) {
            builder.claim("avatarUrl", userDetails.getAvatarUrl());
        }

        if (userDetails.getClientId() != null) {
            builder.claim("client", userDetails.getClientId().toString());
        }

        if (userDetails.getPartnerId() != null) {
            builder.claim("partner", userDetails.getPartnerId().toString());
        }

        return builder.signWith(jwtSecretKey, SignatureAlgorithm.HS512).compact();
    }

    public String createInternalToken() {
        Instant now = Instant.now();
        return Jwts.builder().
                setIssuedAt(Date.from(now)).
                setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES))).
                setSubject(UUID.randomUUID().toString()).
                claim("roles", Collections.singleton(UserDetails.ROLE_SUPERADMIN)).
                signWith(jwtSecretKey, SignatureAlgorithm.HS512).compact();
    }

    public UserDetails parseToken(String token) {

        if (token == null) {
            return null;
        }

        try {

            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            //OK, we can trust this JWT

            UserDetails userDetails =  userDetailsFromClaims(claims);
            userDetails.setToken(token);
            return userDetails;
        } catch (JwtException e) {
            //don't trust the JWT!
            return null;
        }
    }

    private UserDetails userDetailsFromClaims(Claims claims) {
        UserDetails userDetails = new UserDetails();

        String subject = claims.getSubject();

        if (subject != null) {
            userDetails.setId(UUID.fromString(subject));
        }

        String client = claims.get("client", String.class);

        if (client != null) {
            userDetails.setClientId(UUID.fromString(client));
        }

        String partner = claims.get("partner", String.class);

        if (partner != null) {
            userDetails.setPartnerId(UUID.fromString(partner));
        }

        userDetails.setFirstName(claims.get("firstName", String.class));
        userDetails.setLastName(claims.get("lastName", String.class));
        userDetails.setAvatarUrl(claims.get("avatarUrl", String.class));

        Object rolesObj = claims.get("roles");
        Collection<String> roles = null;
        if (rolesObj instanceof Collection) {
            Collection<?> collection = (Collection<?>) rolesObj;
            roles = new ArrayList<>();
            for (Object role : collection) {
                if (role != null) {
                    roles.add(String.valueOf(role));
                }
            }
        }

        if (roles != null) {
            userDetails.setRoles(new HashSet<>(roles));
        }

        return userDetails;
    }


    public String refreshToken(String token) {
        if (token == null) {
            return null;
        }

        try {

            Claims claims = jwtParser.parseClaimsJws(token).getBody();

            Instant exp = claims.getExpiration().toInstant();
            Instant now = Instant.now();
            Duration between = Duration.between(exp.minus(6L, ChronoUnit.MINUTES), now);

            if (between.abs().toMinutes() < 36) {
                //issue new token
                return createToken(userDetailsFromClaims(claims));
            }

            // token still valid
            //todo add human readable error
            return null;
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                Claims claims = ((ExpiredJwtException) e).getClaims();

                Instant exp = claims.getExpiration().toInstant();
                Instant now = Instant.now();
                Duration between = Duration.between(exp, now);

                if (between.abs().toMinutes() < 30) {
                    //issue new token
                    return createToken(userDetailsFromClaims(claims));
                }
            }

            //don't trust the JWT!
            return null;
        }
    }

    public String refreshToken(String token, Date expiresAt) {
        if (token == null) {
            return null;
        }

        try {

            Claims claims = jwtParser.parseClaimsJws(token).getBody();

            Instant exp = claims.getExpiration().toInstant();
            Instant now = Instant.now();
            Duration between = Duration.between(exp.minus(6L, ChronoUnit.MINUTES), now);

            if (between.abs().toMinutes() < 36) {
                //issue new token
                return createToken(userDetailsFromClaims(claims), expiresAt);
            }
            // token still valid
            //todo add human readable error
            return null;
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                Claims claims = ((ExpiredJwtException) e).getClaims();

                Instant exp = claims.getExpiration().toInstant();
                Instant now = Instant.now();
                Duration between = Duration.between(exp, now);

                if (between.abs().toMinutes() < 30) {
                    //issue new token
                    return createToken(userDetailsFromClaims(claims), expiresAt);
                }
            }

            //don't trust the JWT!
            return null;
        }
    }
}
