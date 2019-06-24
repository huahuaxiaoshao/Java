package com.vrkb.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vrkb.bean.User;
import com.vrkb.security.UserValid;
import com.vrkb.service.UserService;
import com.vrkb.utils.JsonResult;

@Controller
@RequestMapping("userController")
public class UserController {

	@Autowired
	private UserService userService;
	private String strCode = "";
	@ModelAttribute
	public void getUser(@RequestParam(value="email",required=false) String userEmail,Map<String,User> map){
		if(userEmail != null){
			User user = userService.getPassword(userEmail); 
			map.put("user", user);
		}
	}
	
	@RequestMapping(value="login")
	public String login(@RequestParam(value="userPassword",required=false) String userPassword,@RequestParam(value="remeber",required=false) boolean remeber,
			@ModelAttribute("user") User user,HttpServletRequest request,HttpServletResponse response,@RequestParam(value="authCode",required=false)String code) throws ServletException, IOException{
		if(user.getId() > 0 && code!=null){
			HttpSession session = request.getSession();
			if(code.equals(strCode)){
				try {
					if(UserValid.loginValid(user.getPassword(),userPassword)){
						session.setAttribute("user", user);
						if(remeber){
							session.setMaxInactiveInterval(7*24*60*60);
						}else{
							session.setMaxInactiveInterval(30*60);
						}
						return "index";
					}
				} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
				request.setAttribute("message", "codeError");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}else{
			try {
				response.sendRedirect(request.getSession().getServletContext().getContextPath()+"/login.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	@RequestMapping("logout")
	public String logout(HttpServletRequest request){
		HttpSession session = request.getSession(false);//防止创建session
		if(session != null){
			session.removeAttribute("user");
		}
		return "login";
	} 

	@RequestMapping("register")
	public String register(){
		return "register";
	}

	@RequestMapping("index")
	public String index(){
		return "index";
	}

	@RequestMapping("save")
	public String save(HttpServletRequest request,@RequestParam(value="userEmail",required=false)String email,@RequestParam(value="userPassword",required=false)String password,@RequestParam(value="userPassword2",required=false)String password2,@RequestParam(value="userPhone",required=false)String phone,
			HttpServletResponse response,@RequestParam(value="authCode",required=false)String code) throws ServletException, IOException{
		if(email != null){
			HttpSession session = request.getSession();
			User user = userService.getPassword(email);
			if(code.equals(strCode)){
				if(user == null){
					if(password.equals(password2)){
						user = new User();
						user.setEmail(email);
						user.setName(email);
						user.setPassword(password);
						user.setPhone(phone);
						user.setGrade("low");
						boolean flag = UserValid.registerUser(userService,user);
						if(flag){
							session.setAttribute("user", user);
							session.setMaxInactiveInterval(30*60);
							return "index";
						}else{
							request.setAttribute("message", "twoInputPasswordNotSame");
							return "redirect:/userController/register";
						}
					}
				}else{
					request.setAttribute("message", "userAlreadyExits");
					return "redirect:/userController/register";
				}
			}else{
				request.setAttribute("message", "codeError");
				return "redirect:/userController/register";
			}
		}
		return "redirect:/userController/register";
	}

	@ResponseBody
	@RequestMapping(value="validUserName",method=RequestMethod.POST)
	public JsonResult validUserName(@RequestParam(value="userName",required=false)String userName){
		JsonResult result = new JsonResult();
		User user = userService.getPassword(userName);
		if(user != null){
			result.setStatus(1);
		}else{
			result.setStatus(0);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value="getCode",method=RequestMethod.POST)
	public JsonResult getCode(@RequestParam(value="authCode",required=false)String code){
		JsonResult result = new JsonResult();
		if(code != null){
			if(code.equals(strCode)){
				result.setStatus(1);
			}else{
				result.setStatus(0);
			}
		}else{
			result.setStatus(0);
		}
		return result;
	}


	@RequestMapping(value="authCode")
	public void getAuthCode(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int width = 63;
		int height = 37;
		Random random = new Random();
		//设置response头信息
		//禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		//生成缓冲区image类
		BufferedImage image = new BufferedImage(width, height, 1);
		//产生image类的Graphics用于绘制操作
		Graphics g = image.getGraphics();
		//Graphics类的样式
		g.setColor(this.getRandColor(200, 250));
		g.setFont(new Font("Times New Roman",0,28));
		g.fillRect(0, 0, width, height);
		//绘制干扰线
		for(int i=0;i<40;i++){
			g.setColor(this.getRandColor(130, 200));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}

		//绘制字符
		strCode = "";
		for(int i=0;i<4;i++){
			String rand = String.valueOf(random.nextInt(10));
			strCode = strCode + rand;
			g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
			g.drawString(rand, 13*i+6, 28);
		}
		//将字符保存到session中用于前端的验证
		request.getSession().setAttribute("strCode", strCode);
		g.dispose();

		ImageIO.write(image, "JPEG", response.getOutputStream());
		response.getOutputStream().flush();
	}

	//创建颜色
	private Color getRandColor(int fc,int bc){
		Random random = new Random();
		if(fc>255)
			fc = 255;
		if(bc>255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r,g,b);
	}

}
