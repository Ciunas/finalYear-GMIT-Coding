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
@WebServlet(name = "AddCustomer", urlPatterns = {"/AddCustomer"})
public class AddCustomer extends HttpServlet {

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
             Class.forName("com.mysql.jdbc.Driver");  // load the driverv
             Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Customer?autoReconnect=true&useSSL=false;user=AppletTest;password=AppletTest;");
                  
             PreparedStatement add = con.prepareStatement("Insert Into Customers Values (?,?,?,?,?,?,?,?,?)");
             add.setString(1, c.id);
             add.setString(2, c.firstName);
             add.setString(3, c.lastName);
             add.setString(4, c.address1);
             add.setString(5, c.address2);
             add.setString(6, c.city);
             add.setString(7, c.state);
             add.setString(8, c.zip);
             add.setString(9, c.phone);
             count  = add.executeUpdate();
            
             add.close();
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
