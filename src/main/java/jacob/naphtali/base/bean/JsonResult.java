package jacob.naphtali.base.bean;

import java.io.Serializable;

import jacob.naphtali.base.bean.util.StringUtils;
import jacob.naphtali.base.constant.YesNo;

/**
 * 用于封装JSON返回数据的值对象类
 */
public class JsonResult <T> 
	implements Serializable{
	private static final long serialVersionUID = 7115787416741580371L;
	
	private short success;
	private String message;
	private T data;
	
	public JsonResult() {
	}
	
	public JsonResult(Exception e){
		this(YesNo.NO, e.getMessage(), null);
	}
	
	public JsonResult(String errorMessage){
		this(YesNo.NO, errorMessage, null);
	}
	
	public JsonResult(T data){
		this(YesNo.YES, "", data);
	}
	
	public JsonResult(
			short success, String message) {
		this(success, message, null);
	}

	

	public JsonResult(short success, String message, T data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}


	public short getSuccess() {
		return success;
	}

	public void setSuccess(short success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return StringUtils.beanString(this);
	}
	
}



