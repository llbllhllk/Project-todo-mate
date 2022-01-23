package ToDoMate.ToDoMate.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        if (request.getSession().getAttribute("id") == null){
//            response.sendRedirect("/");
//            return false;
//        }
//        return true;
//    }
}
