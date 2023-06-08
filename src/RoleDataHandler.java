package com.backcaps.vehiclemanagement;


import java.awt.Component;
import java.io.IOException;
import java.sql.*;
import javax.swing.JOptionPane;



/**
 * @author MacBookPro
 * @version 1.0
 * @created 09-Jan-2023 9:36:29 PM
 */
public class RoleDataHandler {

        String role_database = "jdbc:mysql://localhost:3306/VehicleMGMT";
        String username = "root";
        String password = "";
	public RoleDataHandler(){
	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param index
	 * @param comp
	 */
	public boolean deleteRole(int index, Component comp){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(role_database,username,password);
                String query ="SELECT * FROM Roles";
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery(query);
                rs.absolute(index+1);
                query = "DELETE FROM Roles WHERE ID='"+rs.getInt("ID")+"' AND Name='"+rs.getString("Name")+"'";
                PreparedStatement ptst = conn.prepareCall(query);
                ptst.execute();
                conn.close();
                return true;
            }catch(Exception e){
                JOptionPane.showMessageDialog(comp, e);
                return false;
            }
	}

	/**
	 * 
	 * @param role
	 * @param comp
	 */
	public boolean insertRole(Role role, Component comp){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(role_database,username,password);
                String query = "INSERT INTO Roles VALUES(?,?,?,?,?,?)";
                PreparedStatement ptst = conn.prepareStatement(query);
                ptst.setString(1,role.getId()+"");
                ptst.setString(2,role.getName());
                AccessRights rights = role.getRights();
                ptst.setString(3,rights.isCanCreate() ? 1+"" : 0+"" );
                ptst.setString(4,rights.isCanDelete()? 1+"": 0+"");
                ptst.setString(5,rights.isCanEdit()? 1+"" : 0+"" );
                ptst.setString(6,rights.isCanRead()? 1+"" : 0+"" );
                ptst.executeUpdate();
                JOptionPane.showMessageDialog(comp, "Data Saved Successfully");
                conn.close();
                return true;
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(comp, e);
                return false;
            }
	}

	/**
	 * 
	 * @param index
	 * @param comp
	 */
	public Role searchRole(int index, Component comp){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(role_database,username,password);
                String query = "SELECT * FROM Roles";
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery(query);
                rs.absolute(index+1);
                Role nRole = new Role();
                nRole.setId(rs.getInt("ID"));
                nRole.setName(rs.getString("Name"));
                AccessRights rights = new AccessRights(rs.getBoolean("CanCreate"),rs.getBoolean("CanDelete"),rs.getBoolean("CanEdit"),rs.getBoolean("CanRead"));
                nRole.setRights(rights);
                conn.close();
                return nRole;
            }catch(Exception e){
                JOptionPane.showMessageDialog(comp,e+" Error in readRole");
                return null;
            }
	}

	/**
	 * 
	 * @param name
	 * @param comp
	 */
	public int searchRole(String name, Component comp){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(role_database,username,password);
                String query = "SELECT EXISTS(SELECT * FROM Roles WHERE Name = '"+name+"')";
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                int count = 0;
                //Check if the spesified person found in the database
                if (!st.execute(query)) throw new IOException("Role Not Found");
                //if found then return its position index
                else {
                    query ="SELECT * FROM Role";
                    ResultSet rs = st.executeQuery(query);
                    while(rs.next()){
                        if(rs.getString("Name").equals(name)){
                            conn.close();
                            break;
                        }else count++;
                    }
                return count;
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(comp, e);
                return -1;
            }
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param rights
	 * @param comp
	 */
	public boolean updateRole(int id, String name,int index, AccessRights rights, Component comp){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(role_database,username,password);
                String query ="SELECT * FROM Roles";
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery(query);
                rs.absolute(index+1);
                query = "UPDATE Record SET ID='"+id+"', Name ='"+name+"', CanCreate='"+rights.isCanCreate()+"' CanDelete='"+rights.isCanDelete()+"' CanEdit='"+rights.isCanEdit()+"' CanRead='"+rights.isCanRead()+"' WHERE ID='"+rs.getInt("ID")+"' AND Name='"+rs.getString("Name")+"'";
                PreparedStatement stn = conn.prepareCall(query);
                stn.execute();
                conn.close();
                return true;
            }catch(Exception e){
                JOptionPane.showMessageDialog(comp, e+" Error in Update function");
                return false;
            }
	}
        public int getSize(Component comp){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(role_database,username,password);
                String query ="SELECT COUNT(*) FROM Roles";
                PreparedStatement st = conn.prepareStatement(query);
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    return rs.getInt(1);
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(comp, e+" Error in SizeFunction");
                return -1;
            }
        return 0;
        }
}//end RoleRegister