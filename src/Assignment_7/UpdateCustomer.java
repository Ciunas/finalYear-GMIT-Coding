package Assignment_7;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mark Pendergast
 */
@WebServlet(urlPatterns = {"/UpdateCustomer"})
public class UpdateCustomer extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
         int count = 0;
         response.setContentType("application/octet-stream");
         InputStream in = request.getInputStream();
         ObjectInputStream ois = new ObjectInputStream(in);
         OutputStream outstr = response.getOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(outstr);         
         
         try{
                
            Customer c = (Customer)ois.readObject();
           
            if(c != null)
            {
             Class.forName("ccom.mysql.jdbc.Driver");  // load the driver
             Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Customer;user=AppletTest;password=AppletTest;");
             PreparedStatement update = con.prepareStatement("Update Customers set LAST_NAME=?, FIRST_NAME=?,Street1=?,Street2=?,City=?,State=?,Zip=?,Phone=? where ID =?");
                              
             update.setString(1, c.lastName);
             update.setString(2, c.firstName);
             update.setString(3, c.address1);
             update.setString(4, c.address2);
             update.setString(5, c.city);
             update.setString(6, c.state);
             update.setString(7, c.zip);
             update.setString(8, c.phone);
             update.setString(9, c.id);
             count = update.executeUpdate();
            
             update.close();
             con.close();
            }           
  
        } 
        catch(Exception ex)
        {
          System.out.println(ex.toString());
        }
         finally {   
            oos.writeInt(count);             
            oos.flush();
            oos.close();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
