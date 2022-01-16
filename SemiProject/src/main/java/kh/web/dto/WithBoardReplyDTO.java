package kh.web.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WithBoardReplyDTO {
	private int with_replyseq;
	private int with_replypseq;
	private String with_replyer;
	private String with_replytext;
	private Timestamp with_replydate;
	
	public WithBoardReplyDTO() {}
	public WithBoardReplyDTO(int with_replyseq, int with_replypseq, String with_replyer, String with_replytext,
			Timestamp with_replydate) {
		super();
		this.with_replyseq = with_replyseq;
		this.with_replypseq = with_replypseq;
		this.with_replyer = with_replyer;
		this.with_replytext = with_replytext;
		this.with_replydate = with_replydate;
	}
	public int getWith_replyseq() {
		return with_replyseq;
	}
	public void setWith_replyseq(int with_replyseq) {
		this.with_replyseq = with_replyseq;
	}
	public int getWith_replypseq() {
		return with_replypseq;
	}
	public void setWith_replypseq(int with_replypseq) {
		this.with_replypseq = with_replypseq;
	}
	public String getWith_replyer() {
		return with_replyer;
	}
	public void setWith_replyer(String with_replyer) {
		this.with_replyer = with_replyer;
	}
	public String getWith_replytext() {
		return with_replytext;
	}
	public void setWith_replytext(String with_replytext) {
		this.with_replytext = with_replytext;
	}
	public Timestamp getWith_replydate() {
		return with_replydate;
	}
	public void setWith_replydate(Timestamp with_replydate) {
		this.with_replydate = with_replydate;
	}
	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		return sdf.format(this.with_replydate.getTime());
	}
	public String getDetailDate() {
		long current_time = System.currentTimeMillis();  //현재의 Timestamp
		long write_time = this.with_replydate.getTime();	// 글이 작성된 시점의 Timestamp
		
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



