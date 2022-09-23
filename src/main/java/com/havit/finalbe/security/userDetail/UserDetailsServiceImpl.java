package com.havit.finalbe.security.userDetail;

import com.havit.finalbe.entity.Member;
import com.havit.finalbe.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = (Member) memberRepository.findByUsername(username).orElse(null);
        if (member == null) {
            return null;
        }else{
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setMember(member);
            return userDetails;
        }

    }

}
