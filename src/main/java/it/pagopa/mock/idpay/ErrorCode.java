package it.pagopa.mock.idpay;

public final class ErrorCode {

    public static final String MODULE_ID = "A00";

    /*
     * Error codes.
     */

    // validation errors
    public static final String ERROR_INITIATIVEID_MUST_NOT_BE_NULL 							                = MODULE_ID + "000001";
    public static final String ERROR_INITIATIVENAME_MUST_NOT_BE_NULL 							            = MODULE_ID + "000002";
    public static final String ERROR_ORGANIZATIONNAME_MUST_NOT_BE_NULL 							            = MODULE_ID + "000003";
    public static final String ERROR_MERCHANTID_MUST_NOT_BE_NULL 							                = MODULE_ID + "000004";
    public static final String ERROR_STATUS_MUST_NOT_BE_NULL 							                    = MODULE_ID + "000005";
    public static final String ERROR_STARTDATE_MUST_NOT_BE_NULL 							                = MODULE_ID + "000006";
    public static final String ERROR_ENDDATE_MUST_NOT_BE_NULL 							                    = MODULE_ID + "000007";
    public static final String ERROR_SERVICEID_MUST_NOT_BE_NULL 							                = MODULE_ID + "000008";
    public static final String ERROR_ENABLED_MUST_NOT_BE_NULL 							                    = MODULE_ID + "000009";
    public static final String ERROR_TRANSACTIONFINALSTATUS_MUST_NOT_BE_NULL 							    = MODULE_ID + "00000A";
    public static final String ERROR_RETRIESSTATUSCHANGES_MUST_NOT_BE_NULL 							        = MODULE_ID + "00000B";


    // rest server validation errors
    public static final String INITIATIVE_MUST_NOT_BE_EMPTY 							                    = MODULE_ID + "000030";


    //Initiative retrieval error
    public static final String INITIATIVES_NOT_FOUND_FOR_MERCHANT 							                = MODULE_ID + "000040";

    // db errors
    public static final String ERROR_STORING_DATA_IN_DB 									            = MODULE_ID + "000060";
    public static final String ERROR_RETRIEVING_DATA_FROM_DB	 							            = MODULE_ID + "000061";

    /*
     * Error descriptions
     */
    public static final String ERROR_CALLING_IDPAY_REST_SERVICES_DESCR = "Error calling IdPay rest service";
    public static final String INITIATIVES_NOT_FOUND_FOR_MERCHANT_DESCR = "Initiatives weren’t found for merchant";


    /*
     * Error messages
     */
    public static final String INITIATIVES_NOT_FOUND_FOR_MERCHANT_MSG = "[" + INITIATIVES_NOT_FOUND_FOR_MERCHANT + "] " + INITIATIVES_NOT_FOUND_FOR_MERCHANT_DESCR;

    private ErrorCode() {
    }
}
