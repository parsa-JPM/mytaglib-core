package dp.tag.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

@Component(property = { "osgi.http.whiteboard.context.path=/",
		"osgi.http.whiteboard.servlet.pattern=/parsa" }, service = Servlet.class)
public class ServletTest extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter writer = resp.getWriter();

		writer.print("HELO WORLD!!!");

	}

}
