package kh.web.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareBoardReplyDTO {
	private int sh_replyseq;
	private int sh_replypseq;
	private String sh_replyer;
	private String sh_replytext;
	private Timestamp sh_replydate;
	
	public int getSh_replyseq() {
		return sh_replyseq;
	}
	public void setSh_replyseq(int sh_replyseq) {
		this.sh_replyseq = sh_replyseq;
	}
	public int getSh_replypseq() {
		return sh_replypseq;
	}
	public void setSh_replypseq(int sh_replypseq) {
		this.sh_replypseq = sh_replypseq;
	}
	public String getSh_replyer() {
		return sh_replyer;
	}
	public void setSh_replyer(String sh_replyer) {
		this.sh_replyer = sh_replyer;
	}
	public String getSh_replytext() {
		return sh_replytext;
	}
	public void setSh_replytext(String sh_replytext) {
		this.sh_replytext = sh_replytext;
	}
	public Timestamp getSh_replydate() {
		return sh_replydate;
	}
	public void setSh_replydate(Timestamp sh_replydate) {
		this.sh_replydate = sh_replydate;
	}
	public ShareBoardReplyDTO() {}
	public ShareBoardReplyDTO(int sh_replyseq, int sh_replypseq, String sh_replyer, String sh_replytext, Timestamp sh_writedate) {
		super();
		this.sh_replyseq = sh_replyseq;
		this.sh_replypseq = sh_replypseq;
		this.sh_replyer = sh_replyer;
		this.sh_replytext = sh_replytext;
		this.sh_replydate = sh_writedate;
	}
	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		return sdf.format(this.sh_replydate.getTime());
	}
	public String getDetailDate() {
		long current_time = System.currentTimeMillis();  //현재의 Timestamp
		long write_time = this.sh_replydate.getTime();	// 글이 작성된 시점의 Timestamp
		
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



