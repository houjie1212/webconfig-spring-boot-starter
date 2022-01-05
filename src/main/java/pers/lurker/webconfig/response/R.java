package pers.lurker.webconfig.response;

import java.util.StringJoiner;

public class R<T> {

    private boolean success;
    private String code;
    private String msg;
    private T data;

    public R() {
    }

    public R(Builder<T> builder) {
        this.success = builder.success;
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
    }

    public static Builder builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private boolean success;
        private String code;
        private String msg;
        private T data;

        public Builder() {
        }

        public R<?> buildCustomize(boolean success, String code, String msg) {
            this.success = success;
            this.code = code;
            this.msg = msg;
            return new R<>(this);
        }

        public R<?> buildOk() {
            this.success = true;
            this.code = ReturnCodeEnum.SUCCESS.getCode();
            this.msg = ReturnCodeEnum.SUCCESS.getMsg();
            return new R<>(this);
        }

        public R<?> buildOk(T data) {
            this.success = true;
            if (data == null) {
                this.code = ReturnCodeEnum.NODATA.getCode();
                this.msg = ReturnCodeEnum.NODATA.getMsg();
            } else {
                this.code = ReturnCodeEnum.SUCCESS.getCode();
                this.msg = ReturnCodeEnum.SUCCESS.getMsg();
                this.data = data;
            }
            return new R<>(this);
        }

        public R<?> buildFail(String code, String msg) {
            this.success = false;
            this.code = code;
            this.msg = msg;
            return new R<>(this);
        }

        public Builder setData(T data) {
            this.data = data;
            return this;
        }

    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", R.class.getSimpleName() + "[", "]")
                .add("success=" + success)
                .add("code='" + code + "'")
                .add("msg='" + msg + "'")
                .add("data=" + data)
                .toString();
    }
}