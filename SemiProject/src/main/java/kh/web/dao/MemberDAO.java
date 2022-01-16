package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.MemberDTO;
import kh.web.utils.EncrpytionUtils;
 

public class MemberDAO {
	private static MemberDAO instance = null;
	public static MemberDAO getInstance() {
		if(instance == null) {
			instance = new MemberDAO();
		}
		return instance;
	}
	private MemberDAO() {}
	// Singleton 관련 코드

	private Connection getConnection() throws Exception{
        Context initCtx = new InitialContext();

		Context envCtx = (Context) initCtx.lookup("java:comp/env");

		DataSource ds = (DataSource) envCtx.lookup("jdbc/oracle");

		
		return ds.getConnection();
	}
//아이디 중복체크 
	public boolean isIdExist(String id) throws Exception{
		String sql = "select * from tbl_member where login_id=?";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, id);
			try(ResultSet rs = pstat.executeQuery()){
				return rs.next();
			}
		}
	}
	//사용자 아이디 채번
	public String selectInsertKey() throws Exception {
		String sql = "select TO_CHAR(SYSDATE, 'YYYYMMDD')|| LPAD(MEMBER_SEQ.NEXTVAL,'5','0') as insertKey from DUAL";
		String insertKey ="";    
        ResultSet rs = null;

		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			rs = pstat.executeQuery();
			while(rs.next()){     
                insertKey = rs.getString("insertKey");
			}
		}
		return insertKey;	
	}
	//회원가입 
	public int insert(MemberDTO dto)throws Exception {
		String sql = "INSERT INTO tbl_member (member_pw,login_id,member_name,member_nickname,member_zipcode,member_address1,member_address2,member_ssn,member_phone,member_email,admin_yn,member_id,avg_age) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,  "
				+ "(select case when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 > 0 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 10"
				+ "            then '0~9'"
				+ "            when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 >= 10 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 20"
				+ "            then '10~20'"
				+ "            when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 >= 20 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 30"
				+ "            then '20~30'"
				+ "            when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 >= 30 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 40"
				+ "            then '30~40'"
				+ "            when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 >= 40 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 50"
				+ "            then '40~50'"
				+ "            when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 >= 50 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 60"
				+ "            then '50~60'"
				+ "            when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 >= 60 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 70"
				+ "            then '60~70'"
				+ "            when MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 >= 70 and MONTHS_BETWEEN(TRUNC(SYSDATE,'YEAR'), TRUNC(TO_DATE('19' || substr('6612051226151','0','6'),'YYYYMMDD'),'YEAR')) /12 +1 < 80"
				+ "            then '70~80'"
				+ "            ELSE '80~'"
				+ "        END AS AVGAGE"
				+ "  from dual DUAL1) )";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){

			pstat.setString(1,dto.getPw());
			pstat.setString(2,dto.getId());
			pstat.setString(3,dto.getName());
			pstat.setString(4,dto.getNickname());
			pstat.setString(5,dto.getZipcode());
			pstat.setString(6,dto.getAddress1());
			pstat.setString(7,dto.getAddress2());
			pstat.setString(8,dto.getSsn());
			pstat.setString(9,dto.getPhone());
			pstat.setString(10,dto.getEmail());
			pstat.setString(11,"N");
			pstat.setString(12,selectInsertKey());
			
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}	
	}
	//카카오 회원가입 
    public int kakaoInsert(MemberDTO dto)throws Exception {
        String sql = "INSERT INTO tbl_member (login_id,member_nickname,member_email,member_id,avg_age,kakao_login_yn,member_name) VALUES (?,?,?,?,?,'Y',?)";
        try(Connection con = this.getConnection();
                PreparedStatement pstat = con.prepareStatement(sql);){

            pstat.setString(1,dto.getEmail());
            pstat.setString(2,dto.getNickname());
            pstat.setString(3,dto.getEmail());
            pstat.setString(4,selectInsertKey()); //멤버 아이디
            pstat.setString(5,dto.getAvgAge());
            pstat.setString(6,dto.getNickname());

            int result = pstat.executeUpdate();
            con.commit();
            return result;
        }
    }
	//회원수정 
	public int update(MemberDTO dto)throws Exception {
		
		// 저장 하는 쿼리.
		String sql = "UPDATE tbl_member SET  member_phone = ?, member_email = ?, member_zipcode = ?, member_address1 = ?, member_address2 = ? WHERE  login_id = ? ";
		
		//디비 컨넥션
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){

			//파라미터 세팅 (화면에서 받아온거.)
			pstat.setString(1,dto.getPhone());
			pstat.setString(2,dto.getEmail());
			pstat.setString(3,dto.getZipcode());
			pstat.setString(4,dto.getAddress1());
			pstat.setString(5,dto.getAddress2());
			pstat.setString(6,dto.getId());
			
			//쿼리 실행(db저장)
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}	
	}

	public boolean isLoginAllowed(String id, String pw) throws Exception{
		String sql = "select * from tbl_member where login_id = ? and member_pw = ?";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){			
			pstat.setString(1, id);
			pstat.setString(2, pw);
			try(ResultSet rs = pstat.executeQuery()){
				return rs.next();
			}
		}
	}

	public int delete(String id) throws Exception{
		String sql = "delete from tbl_member where login_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, id);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	//회원정보 조회
	public MemberDTO selectById(String paramId) throws Exception{
		String sql = "select * from tbl_member where login_id = ?";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setString(1, paramId);
			try(ResultSet rs = pstat.executeQuery()){

				MemberDTO dto = new MemberDTO();
				if(rs.next()) {
					dto.setId(rs.getString("login_id"));
					dto.setName(rs.getString("member_name"));
					dto.setNickname(rs.getString("member_nickname"));
					dto.setZipcode(rs.getString("member_zipcode"));
					dto.setAddress1(rs.getString("member_address1"));
					dto.setAddress2(rs.getString("member_address2"));
					dto.setPhone(rs.getString("member_phone"));
					dto.setEmail(rs.getString("member_email"));
					
					
					
					return dto;
				}
				return null;
			}
		}

	}
	//패스워드 찾기
	
	public boolean searchPw(String id) throws Exception{
		/*
		 * 1.아이디로 이메일찾기
		 * 2.임시비밃번호 생성
		 * 3.임시비밀번호 db저장
		 * 4.임시비밀번호 전송
		 */
		
		//1.아이디로 이메일찾기
		
		String sql = "select * from tbl_member where login_id = ?";
		String email_id = ""; //로그인 아이디 변수 생성  
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, id);
			
			ResultSet rs = pstat.executeQuery();
			while(rs.next()){     
				email_id = rs.getString("member_email");
			}
			
		}
		//2.임시비밀번호 생성
		
		String env_temp_pw =EncrpytionUtils.getSHA512("daedong485868");//임시비밀번호 변수 생성
		String temp_pw ="daedong485868";//임시비밀번호 변수 생성
		
		
		//3.임시비밀번호 db저장
	    sql = "UPDATE tbl_member SET  MEMBER_PW = ? WHERE  login_id = ? ";
		
		//디비 컨넥션
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){

			//파라미터 세팅 (화면에서 받아온거.)
			pstat.setString(1,env_temp_pw);
			pstat.setString(2,id);
			//쿼리 실행(db저장)
			int result = pstat.executeUpdate();
			con.commit();
		}	
		//4.임시비밀번호 전송
		sendMail(email_id,temp_pw);
		
		
		return true;
	}
	public void editPw(String pw, String id) throws Exception{
	    String sql = "UPDATE tbl_member SET  MEMBER_PW = ? WHERE  login_id = ? ";
		
		//디비 컨넥션
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){

			//파라미터 세팅 (화면에서 받아온거.)
			pstat.setString(1,EncrpytionUtils.getSHA512(pw));
			pstat.setString(2,id);
			//쿼리 실행(db저장)
			int result = pstat.executeUpdate();
			con.commit();
		}	
	}

	//메일 전송
	  /**
	   * SendMail
	   */
	  public static void sendMail(String member_email,String temp_pw) {
	    // 메일 인코딩
	    final String bodyEncoding = "UTF-8"; //콘텐츠 인코딩
	    
	    String subject = "대동맛지도 비밀번호 찾기";
	    String fromEmail = "daedongmap@daedong.com";
	    String fromUsername = "SYSTEM MANAGER";
	    String toEmail = member_email; // 콤마(,)로 여러개 나열
	    
	    final String username = "lsm940705@gmail.com";         
	    final String password = "fwihbdhtrhsgzige";
	    
	    // 메일에 출력할 텍스트
	    StringBuffer sb = new StringBuffer();
	    sb.append("<h3>대동맛지도 임시비밀번호 입니다. 로그인해주세요</h3>\n");
	    sb.append("<h4>"+temp_pw+"</h4>\n");    
	    String html = sb.toString();
	    
	    // 메일 옵션 설정
	    Properties props = new Properties();
	    props.put("mail.smtp.auth","true");
	    props.put("mail.smtp.starttls.enable","true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port","587");
	    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		/*
		 * props.put("mail.transport.protocol", "smtp"); props.put("mail.smtp.host",
		 * "smtp.gmail.com"); props.put("mail.smtp.port", "587");
		 * props.put("mail.smtp.auth", "true"); props.put("mail.smtp.quitwait",
		 * "false"); props.put("mail.smtp.socketFactory.port", "465");
		 * props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		 * props.put("mail.smtp.socketFactory.fallback", "false");
		 */
	    
	    try {
	      // 메일 서버  인증 계정 설정
	      Authenticator auth = new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	          return new PasswordAuthentication(username, password);
	        }
	      };
	      
	      // 메일 세션 생성
	      Session session = Session.getInstance(props, auth);
	      
	      // 메일 송/수신 옵션 설정
	      Message message = new MimeMessage(session);
	      message.setFrom(new InternetAddress(fromEmail, fromUsername));
	      message.setRecipients(RecipientType.TO, InternetAddress.parse(toEmail, false));
	      message.setSubject(subject);
	      
	      // 메일 콘텐츠 설정
	      Multipart mParts = new MimeMultipart();
	      MimeBodyPart mTextPart = new MimeBodyPart();
	      MimeBodyPart mFilePart = null;
	 
	      // 메일 콘텐츠 - 내용
	      mTextPart.setText(html, bodyEncoding, "html");
	      mParts.addBodyPart(mTextPart);
	            
	      // 메일 콘텐츠 설정
	      message.setContent(mParts);
	      
	      // MIME 타입 설정
	      MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
	      MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
	      MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
	      MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
	      MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
	      MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
	      CommandMap.setDefaultCommandMap(MailcapCmdMap);
	 
	      // 메일 발송
	      Transport.send( message );
	      
	    } catch ( Exception e ) {
	      e.printStackTrace();
	    }
	  }
	  public String blackYn(String id) throws Exception{
			
			String sql = "select * from tbl_member where login_id = ?";
			String blacklist = ""; //블랙리스트 저장 변수  
			try(Connection con = this.getConnection();
					PreparedStatement pstat = con.prepareStatement(sql);){
				pstat.setString(1, id);
				ResultSet rs = pstat.executeQuery();
				if(rs.next()){     
					blacklist = rs.getString("blacklist");
				}
				return blacklist;
			}
	  }
}

























