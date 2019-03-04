package jacob.naphtali.base.bean;

public class WangEditorMsg {
	
	public static final short SUCCESS = 0;
	public static final short ERROR = 1;
	
	/*
	 * 即错误代码，0 表示没有错误。
	 * 如果有错误，errno != 0，可通过下文中的监听函数 fail 拿到该错误码进行自定义处理
	 */
	private short errno; 
	/*
	 * "data": ["图片1地址",  "图片2地址", "……"]
	 */
	private String[] data;
	
	private WangEditorMsg(short errno, String[] data) {
		super();
		this.errno = errno;
		this.data = data;
	}

	public static WangEditorMsg getInstance(short errno, String... data) {
		return new WangEditorMsg(errno, data);
	}
	
	public short getErrno() {
		return errno;
	}
	public void setErrno(short errno) {
		this.errno = errno;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

}
