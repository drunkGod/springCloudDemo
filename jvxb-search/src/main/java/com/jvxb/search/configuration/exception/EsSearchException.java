package com.jvxb.search.configuration.exception;

import com.jvxb.common.exception.BaseException;

/**
 * @author jvxb
 * @since 2020-06-13
 */
public class EsSearchException extends BaseException{

    private static final long serialVersionUID = 1L;

    public EsSearchException(SEARCH_EXCEPTION ex, Throwable cause){
        super(ex.getCode(),ex.getMessage(),cause);
    }

    public EsSearchException(SEARCH_EXCEPTION ex){
        super(ex.getCode(),ex.getMessage());
    }

    public EsSearchException(int code, String message){
        super(code,message);
    }


    public EsSearchException(String message, Throwable cause){
        super(BaseException.ERROR_CODE,message,cause);
    }

    public EsSearchException(String message){
        super(BaseException.ERROR_CODE,message);
    }

    public EsSearchException(){
        super();
    }

    public enum SEARCH_EXCEPTION{
        ES_CONNECT_ADDRESS_ERROR(700001,"es连接地址错误！"),
        ES_ADD_ERROR(700002,"es新增失败！"),
        ES_DEL_ERROR(700003,"es删除失败！"),
        ES_QUERY_ERROR(700004,"es查询失败！"),
        ES_UPDATE_ERROR(700005,"es更新失败！"),
        ES_OPTIMIZE_ERROR(700006,"es优化失败"),
        ES_QUERY_RESULT_ERROR(700007,"es查询结果错误");
        private SEARCH_EXCEPTION(int code, String message) {
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
