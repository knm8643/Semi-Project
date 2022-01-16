package kh.web.dto;

import java.sql.Timestamp;

public class RecipeBoardReplyDTO {
	private int rc_replyseq;
	private int rc_replypseq;
	private String rc_replyer;
	private String rc_replytext;
	private Timestamp rc_replydate;
	
	public RecipeBoardReplyDTO() {}
	public RecipeBoardReplyDTO(int rc_replyseq, int rc_replypseq, String rc_replyer, String rc_replytext,
			Timestamp rc_replydate) {
		super();
		this.rc_replyseq = rc_replyseq;
		this.rc_replypseq = rc_replypseq;
		this.rc_replyer = rc_replyer;
		this.rc_replytext = rc_replytext;
		this.rc_replydate = rc_replydate;
	}
	public int getRc_replyseq() {
		return rc_replyseq;
	}
	public void setRc_replyseq(int rc_replyseq) {
		this.rc_replyseq = rc_replyseq;
	}
	public int getRc_replypseq() {
		return rc_replypseq;
	}
	public void setRc_replypseq(int rc_replypseq) {
		this.rc_replypseq = rc_replypseq;
	}
	public String getRc_replyer() {
		return rc_replyer;
	}
	public void setRc_replyer(String rc_replyer) {
		this.rc_replyer = rc_replyer;
	}
	public String getRc_replytext() {
		return rc_replytext;
	}
	public void setRc_replytext(String rc_replytext) {
		this.rc_replytext = rc_replytext;
	}
	public Timestamp getRc_replydate() {
		return rc_replydate;
	}
	public void setRc_replydate(Timestamp rc_replydate) {
		this.rc_replydate = rc_replydate;
	}
	
}
