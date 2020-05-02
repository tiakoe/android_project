package com.a.http_module.http;

/**
 * 异常处理
 */
class ApiException extends RuntimeException {

    private static final int USER_NOT_EXIST = 100;
    private static final int WRONG_PASSWORD = 101;

    ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    private ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code 错误码
     * @return 错误信息
     */
    private static String getApiExceptionMessage(int code) {
        String message;
        switch (code) {
            case USER_NOT_EXIST:
                message = "该用户不存在";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;
            default:
                message = "未知错误";

        }
        return message;
    }
}

