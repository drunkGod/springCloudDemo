package com.jvxb.common.exception;


public class ArgmentNotValidException extends BaseException {

    /**
     * 参数验证异常
     */
    private static final long serialVersionUID = 1L;

    public ArgmentNotValidException(ARGUMENT_VALI_EXP ex, Throwable cause) {
        super(ex.getCode(), ex.getMessage(), cause);
    }

    public ArgmentNotValidException(ARGUMENT_VALI_EXP ex) {
        super(ex.getCode(), ex.getMessage());
    }

    public ArgmentNotValidException(int code, String message) {
        super(code, message);
    }

    public ArgmentNotValidException(String message) {
        super(BaseException.ERROR_CODE, message);
    }

    public ArgmentNotValidException() {
        super();
    }

    /**
     * 异常类枚举
     */
    public enum ARGUMENT_VALI_EXP {
        PARAMETER_CHECK_ERROR(500001, "参数校验异常"),
        STRING_NOT_NULL(500002, "字符串校验异常-不能为空"),
        OBJECT_NOT_NULL(500003, "对象校验异常-不能为空"),
        MAP_NOT_NULL(500004, "MAP校验异常-不能为空"),
        COLLECTION_NOT_NULL(500005, "数组集合校验异常-不能为空"),
        BOOLEAN_NOT_FALSE(500006, "是否校验异常-不能为否"),
        NUMBER_RANGE_ERROR(500007, "数字校验异常-范围错误");

        private ARGUMENT_VALI_EXP(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
