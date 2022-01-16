package kh.web.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class QnAReplyDTO {
	private int seq;
	private int parentSeq;
	private String writer;
	private String contents;
	private Timestamp write_date;
	public QnAReplyDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QnAReplyDTO(int seq, int parentSeq, String writer, String contents, Timestamp write_date) {
		super();
		this.seq = seq;
		this.parentSeq = parentSeq;
		this.writer = writer;
		this.contents = contents;
		this.write_date = write_date;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getParentSeq() {
		return parentSeq;
	}
	public void setParentSeq(int parentSeq) {
		this.parentSeq = parentSeq;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Timestamp getWrite_date() {
		return write_date;
	}
	public void setWrite_date(Timestamp write_date) {
		this.write_date = write_date;
	}
	public String getFormedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		return sdf.format(this.write_date.getTime());
	}
	public String getDetailDate() {
		long current_time = System.currentTimeMillis();  //현재의 Timestamp
		long write_time = this.write_date.getTime();	// 글이 작성된 시점의 Timestamp
		
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
