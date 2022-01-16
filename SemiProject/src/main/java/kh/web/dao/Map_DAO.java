package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.Map_DTO;

public class Map_DAO {
	private static Map_DAO instance = null;
	public static Map_DAO getIntance() {
		if(instance==null) {
			instance = new Map_DAO();
		}
		return instance;
	}
	private Map_DAO() {}

	private Connection getConection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}




	// Map_index에서 장소등록 버튼을 클릭 하면 반응 
	public int insert(Map_DTO dto) throws Exception {
		String sql = "insert into map_api values (map_api_id.NEXTVAL,?,?,?,?)";
		//		String sql = "insert into map_api values (MAP_API_ID.NEXTVAL,?,?,?) into ser_map values (ser_map_id.nextval,?,?,?) select * from dual";
		try(Connection con = this.getConection();
				PreparedStatement pstat = con.prepareStatement(sql);){

			pstat.setString(1, dto.getName());
			pstat.setString(2, dto.getAddress_name());
			pstat.setString(3, dto.getTel());
			pstat.setString(4, dto.getUser_name());

			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	// 관리자 테이블에 자동으로 사용자가 저장한 값 또한 등록(데이터 산출위한)
	public int getRecord() throws Exception {
		String sql = "insert into ser_map select * from map_api";
		try(Connection con = this.getConection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}


//	 my_mappage에서 등록한 정보들을 출력
//		public List<Map_DTO> selectAll() throws Exception {
//			String sql = "select * from map_api where rownum <= 9";
//			try(Connection con = this.getConection();
//					PreparedStatement pstat = con.prepareStatement(sql);
//					ResultSet rs = pstat.executeQuery()){
//				List<Map_DTO> list = new ArrayList<> ();
//				while(rs.next()) {
//					Map_DTO dto = new Map_DTO();
//					dto.setId(rs.getInt("id"));
//					dto.setName(rs.getString("name"));
//					dto.setAddress_name(rs.getString("address_name"));
//					dto.setTel(rs.getString("tel"));
//					list.add(dto);
//				}
//				return list;
//			}
//		}

	// 사용자가 화면에서 삭제 버튼을 누르면 지워짐 
	public int delete(int id) throws Exception {
		String sql = "delete from map_api where id =?";
		try(Connection con = this.getConection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setInt(1, id);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	// 검색한 특정정보를 출력 
	public List<Map_DTO> map_select(String address_name) throws Exception {
		String sql = "select*from(select distinct name, address_name, tel from ser_map order by dbms_random.value) where address_name like '%'||?||'%' and rownum <=10";
		//		String sql = "select distinct name, address_name, tel from ser_map where address_name like '%'||?||'%' and id between 1 and 10 order by address_name";
		//		String sql = "select * from map_api where address_name like '%'||?||'%'";
		try(Connection con = this.getConection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, address_name);
			try(ResultSet rs = pstat.executeQuery()){

				List<Map_DTO> list_map = new ArrayList<> ();
				while(rs.next()) {
					Map_DTO dto = new Map_DTO();
					dto.setName(rs.getString("name"));
					dto.setAddress_name(rs.getString("address_name"));
					dto.setTel(rs.getString("tel"));
					list_map.add(dto);
				}
				return list_map;
			}
		}
	}

	public List<Map_DTO> selectAll(String user) throws Exception {
		String sql = "select id, name, address_name, tel from map_api where rownum <= 9 and map_user = ? and rowid in(select max(rowid) from map_api group by name)";
		try(Connection con = this.getConection();
				PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, user);
			try(ResultSet rs = pstat.executeQuery()){
				List<Map_DTO> list = new ArrayList<> ();
				while(rs.next()) {
					Map_DTO dto = new Map_DTO();
					dto.setId(rs.getInt("id"));
					dto.setName(rs.getString("name"));
					dto.setAddress_name(rs.getString("address_name"));
					dto.setTel(rs.getString("tel"));
					list.add(dto);
				}
				return list;


			}
		}

	}


}
