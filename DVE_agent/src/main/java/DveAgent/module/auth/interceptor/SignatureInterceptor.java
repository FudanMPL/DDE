package DveAgent.module.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.druid.util.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import DveAgent.common.MyRequestWrapper;
import DveAgent.common.R;
import DveAgent.common.Utlis;

@Component
public class SignatureInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull Object handler)
      throws Exception {
    MyRequestWrapper wrapper = new MyRequestWrapper(request);
    String s = IOUtils.toString(wrapper.getReader());
    R<String> r = JSON.parseObject(s, new TypeReference<R<String>>() {
    });
    // Utlis.verifyData(R.serialize(r), r.getAuth(), null);
    return HandlerInterceptor.super.preHandle(wrapper, response, handler);
  }
}
