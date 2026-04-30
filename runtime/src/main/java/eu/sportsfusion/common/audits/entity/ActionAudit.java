package eu.sportsfusion.common.audits.entity;

/**
 * TODO: stab for now, actions needed should be reviewed
 * Model object for logging user/system actions.
 *
 * @author Created by Mikhail Lysenka on 30.03.16.
 */
public class ActionAudit {

    /* Actions required
    Suggested frames:
    PRODUCTS: 100-119
    USERS: 120-139
    TOURS: 140-159
    INVOICING: 160-169
    BOOKINGS: 170-199
    ...
        stored in DB using eu.sportsfusion.common.micro.quarkus.runtime.common.audits.entity.Action class
     */

    // List of actions from old tours:
    public static final Long INVOICED_MONTH = 1L;
    public static final Long REINVOICED_MONTH = 2L;
    public static final Long INVOICE_PAID = 3L;
    public static final Long INVOICE_RESCHEDULED = 4L;
    public static final Long DELETED_AGENT = 5L;
    public static final Long DELETED_VOUCHER_TYPE = 20L;
    public static final Long UPDATED_3RD_PARTY_PATTERN = 21L;
    public static final Long UPDATED_3RD_PARTY_NAME = 22L;
    public static final Long CREATED_3RD_PARTY = 23L;
    public static final Long DELETED_EXTRA_CLOSURE = 40L;
    public static final Long TOUR_TYPE_RENAMED = 50L;
    public static final Long BOOKING_REFUNDED = 60L;
    public static final Long BOOKING_RESCHEDULED = 61L;
    public static final Long BOOKING_CONFIRMATION_RESENT = 62L;
    public static final Long BOOKING_QUANTITIES_ADDED = 63L;
    public static final Long BOOKING_CUSTOMER_UPDATED = 64L;
    public static final Long BOOKING_ARRIVED_CHANGED = 65L;
    public static final Long BOOKING_INSTALLMENT_ADDED = 66L;
    public static final Long BOOKING_ITEM_CANCELLED = 67L;
    public static final Long BOOKING_TICKETS_PRINTED = 68L;
    public static final Long BOOKING_TICKETS_REPRINTED = 69L;
    public static final Long TOUR_RESCHEDULED = 70L;
    public static final Long TOURS_POPULATED = 71L;
    public static final Long FEEDBACK_LEFT = 80L;
    public static final Long FEEDBACK_UPDATED = 81L;
    public static final Long TOUR_BOOKED_QUANTITY_DECREASED = 90L;
    public static final Long TOUR_BOOKED_QUANTITY_INCREASED = 91L;
    public static final Long TOUR_BOOKED_QUANTITY_BREAKDOWN_CHANGED = 92L;
    public static final Long TOUR_ARRIVED_CHANGED = 95L;

}
