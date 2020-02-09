package dp.tag.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import dp.tag.helpers.Captcha;
import dp.tag.helpers.CaptchaAPI;



@Component(property = { "osgi.http.whiteboard.context.path=/",
"osgi.http.whiteboard.servlet.pattern=/captcha-refresh" }, service = Servlet.class)
public class CaptchaRefreshAjax extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		CaptchaAPI api = new CaptchaAPI();
		Captcha captcha = api.getNew();
		
		req.getSession().setAttribute("captchaId", captcha.getId());
		
		PrintWriter writer = resp.getWriter();

		writer.print(captcha.getImage());

	}
}
