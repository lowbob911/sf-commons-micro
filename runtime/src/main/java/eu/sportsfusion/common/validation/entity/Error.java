package eu.sportsfusion.common.validation.entity;

import java.util.Objects;

/**
 * @author Anatoli Pranovich
 */
public class Error {

    private String code;
    private String source;
    private String title;
    private String detail;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Error)) return false;
        Error error = (Error) o;
        return Objects.equals(code, error.code) &&
                Objects.equals(source, error.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, source);
    }
}
