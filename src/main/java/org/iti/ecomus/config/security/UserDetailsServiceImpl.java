//package org.iti.ecomus.config.security;
//
//
//import org.iti.ecomus.repository.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.iti.ecomus.entity.User;
//
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRepo userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username)
//            throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("Could not find user");
//        }
//
//        return new MyUserDetails(user);
//    }
//
//}
