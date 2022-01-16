package kh.web.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RecipeBoardDTO {
	private int rc_seq;
	private String rc_writer;
	private String rc_title;
	private String rc_contents;
	private Timestamp rc_writedate;
	private int rc_viewcount;

	public int getRc_seq() {
		return rc_seq;
	}
	public void setRc_seq(int rc_seq) {
		this.rc_seq = rc_seq;
	}
	public String getRc_writer() {
		return rc_writer;
	}
	public void setRc_writer(String rc_writer) {
		this.rc_writer = rc_writer;
	}
	public String getRc_title() {
		return rc_title;
	}
	public void setRc_title(String rc_title) {
		this.rc_title = rc_title;
	}
	public String getRc_contents() {
		return rc_contents;
	}
	public void setRc_contents(String rc_contents) {
		this.rc_contents = rc_contents;
	}
	public Timestamp getRc_writedate() {
		return rc_writedate;
	}
	public void setRc_writedate(Timestamp rc_writedate) {
		this.rc_writedate = rc_writedate;
	}
	public int getRc_viewcount() {
		return rc_viewcount;
	}
	public void setRc_viewcount(int rc_viewcount) {
		this.rc_viewcount = rc_viewcount;
	}
	

	public RecipeBoardDTO() {}
	public RecipeBoardDTO(int rc_seq, String rc_writer, String rc_title, String rc_contents, Timestamp rc_writedate, int rc_viewcount
			) {
		super();
		this.rc_seq = rc_seq;
		this.rc_writer = rc_writer;
		this.rc_title = rc_title;
		this.rc_contents = rc_contents;
		this.rc_writedate = rc_writedate;
		this.rc_viewcount = rc_viewcount;

		
	}
	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(this.rc_writedate.getTime());
	}

	public String getDetailDate() {
		long current_time = System.currentTimeMillis(); // 현재의 Timestamp
		long write_time = this.rc_writedate.getTime(); // 글이 작성된 시점의 Timestamp
		
		long time_gap = current_time - write_time;
		
		if(time_gap < 60000) {
			return "1분 이내";
		}else if(time_gap < 300000) {
			return "5분 이내";
		}else if(time_gap < 3600000) {
			return "1시간 이내";
		}else if(time_gap < 86400000) {
			return "오늘";
		}else {
			return getFormedDate();
		}
		
	}

}
