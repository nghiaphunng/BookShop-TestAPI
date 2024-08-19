package com.bookshop.demo.service;

import com.bookshop.demo.request.AuthenticationRequest;
import com.bookshop.demo.request.IntrospectRequest;
import com.bookshop.demo.response.AuthenticationResponse;
import com.bookshop.demo.response.IntrospectResponse;

public interface AuthenticationService {
	AuthenticationResponse authenticate(AuthenticationRequest request); //tạo và kí token
	IntrospectResponse introspect(IntrospectRequest request); //xác thực token
}
