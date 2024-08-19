package com.bookshop.demo.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bookshop.demo.entity.User;
import com.bookshop.demo.repository.UserRepository;
import com.bookshop.demo.request.AuthenticationRequest;
import com.bookshop.demo.request.IntrospectRequest;
import com.bookshop.demo.response.AuthenticationResponse;
import com.bookshop.demo.response.IntrospectResponse;
import com.bookshop.demo.service.AuthenticationService;
import com.bookshop.demo.utils.ProcessPassword;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	@Autowired
	private UserRepository userRepository;
	
	@Value("${jwt.signerKey}")
	private String signKey;
	
	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		User user = userRepository.findByUserName(request.getUserName());
		
		boolean authenticated = ProcessPassword.isPasswordMatch(request.getUserPassword(), user.getUserPassword());
		if(!authenticated) {
			return AuthenticationResponse.builder()
											.authenticated(false)
											.build();
		}
		else {
			return AuthenticationResponse.builder()
					.authenticated(true)
					.token(generateToken(request.getUserName()))
					.build();
		}
	}
	
	private String generateToken(String userName) {
		JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
		
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
											.subject(userName)
											.issuer("bookshop.com")
											.issueTime(new Date())
											.expirationTime(new Date(
													Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
											.claim("customeClaim", "Custom")
											.build();
		
		Payload payload = new Payload(jwtClaimsSet.toJSONObject());
		
		JWSObject jwsObject = new JWSObject(jwsHeader, payload);
		
		try {
			jwsObject.sign(new MACSigner(signKey.getBytes())); //dung de ky va giai ma luon
			return jwsObject.serialize();
			
		} catch (KeyLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

	@Override
	public IntrospectResponse introspect(IntrospectRequest request) {
		String token = request.getToken();
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWSVerifier jwsVerifier = new MACVerifier(signKey.getBytes());
			Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
			boolean checkVerify = signedJWT.verify(jwsVerifier);

			return IntrospectResponse.builder()
					.valid(checkVerify && expiryTime.after(new Date()))
					.build();
		}
		 catch (Exception e) {
			// TODO: handle exception
		}
		return 	IntrospectResponse.builder()
						.valid(false)
						.build();
	}
}
