package kh.web.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class WithBoardDTO {
	private int with_seq;
	private String with_writer;
	private String with_title;
	private String with_contents;
	private Timestamp with_writedate;
	private int with_viewcount;
	
	
	public WithBoardDTO() {}
	public WithBoardDTO(int with_seq, String with_writer, String with_title, String with_contents,
			Timestamp with_writedate, int with_viewcount) {
		super();
		this.with_seq = with_seq;
		this.with_writer = with_writer;
		this.with_title = with_title;
		this.with_contents = with_contents;
		this.with_writedate = with_writedate;
		this.with_viewcount = with_viewcount;
	}

	public int getWith_seq() {
		return with_seq;
	}

	public void setWith_seq(int with_seq) {
		this.with_seq = with_seq;
	}

	public String getWith_writer() {
		return with_writer;
	}

	public void setWith_writer(String with_writer) {
		this.with_writer = with_writer;
	}

	public String getWith_title() {
		return with_title;
	}

	public void setWith_title(String with_title) {
		this.with_title = with_title;
	}

	public String getWith_contents() {
		return with_contents;
	}

	public void setWith_contents(String with_contents) {
		this.with_contents = with_contents;
	}

	public Timestamp getWith_writedate() {
		return with_writedate;
	}

	public void setWith_writedate(Timestamp with_writedate) {
		this.with_writedate = with_writedate;
	}

	public int getWith_viewcount() {
		return with_viewcount;
	}

	public void setWith_viewcount(int with_viewcount) {
		this.with_viewcount = with_viewcount;
	}

	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(this.with_writedate.getTime());
	}

	public String getDetailDate() {
		long current_time = System.currentTimeMillis(); // 현재의 Timestamp
		long write_time = this.with_writedate.getTime(); // 글이 작성된 시점의 Timestamp
		
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
