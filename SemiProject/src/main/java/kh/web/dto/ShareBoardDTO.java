package kh.web.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ShareBoardDTO {
	private int sh_seq;
	private String sh_writer;
	private String sh_title;
	private String sh_contents;
	private Timestamp sh_writedate;
	private int sh_viewcount;
	
	public ShareBoardDTO() {}
	public ShareBoardDTO(int sh_seq, String sh_writer, String sh_title, String sh_contents, Timestamp sh_writedate,
			int sh_viewcount) {
		super();
		this.sh_seq = sh_seq;
		this.sh_writer = sh_writer;
		this.sh_title = sh_title;
		this.sh_contents = sh_contents;
		this.sh_writedate = sh_writedate;
		this.sh_viewcount = sh_viewcount;
	}

	public int getSh_seq() {
		return sh_seq;
	}

	public void setSh_seq(int sh_seq) {
		this.sh_seq = sh_seq;
	}

	public String getSh_writer() {
		return sh_writer;
	}

	public void setSh_writer(String sh_writer) {
		this.sh_writer = sh_writer;
	}

	public String getSh_title() {
		return sh_title;
	}

	public void setSh_title(String sh_title) {
		this.sh_title = sh_title;
	}

	public String getSh_contents() {
		return sh_contents;
	}

	public void setSh_contents(String sh_contents) {
		this.sh_contents = sh_contents;
	}

	public Timestamp getSh_writedate() {
		return sh_writedate;
	}

	public void setSh_writedate(Timestamp sh_writedate) {
		this.sh_writedate = sh_writedate;
	}

	public int getSh_viewcount() {
		return sh_viewcount;
	}

	public void setSh_viewcount(int sh_viewcount) {
		this.sh_viewcount = sh_viewcount;
	}

	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(this.sh_writedate.getTime());
	}

	public String getDetailDate() {
		long current_time = System.currentTimeMillis(); // 현재의 Timestamp
		long write_time = this.sh_writedate.getTime(); // 글이 작성된 시점의 Timestamp
		
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
