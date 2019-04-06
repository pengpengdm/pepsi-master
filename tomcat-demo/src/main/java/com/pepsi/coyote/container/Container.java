package com.pepsi.coyote.container;


import com.pepsi.coyote.http.HttpRequest;
import com.pepsi.coyote.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by wanqinfeng on 2018/1/20.
 */
public interface Container {

    void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException;

}
