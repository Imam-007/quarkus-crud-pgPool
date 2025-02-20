//package org.imam.resources;
//
//import jakarta.inject.Inject;
//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.Produces;
//import jakarta.ws.rs.core.MediaType;
//import org.imam.utils.JwtUtil;
//
//@Path("")
//public class TokenResource {
//
//    @Inject
//    JwtUtil jwtUtil;
//
//    @GET
//    @Path("/token")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String getToken(){
//        return jwtUtil.generateToken();
//    }
//}
//
