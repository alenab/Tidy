package ca.tidygroup.service;

import ca.tidygroup.model.Account;
import ca.tidygroup.model.SecurityUserDetails;
import ca.tidygroup.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SecurityUserDetailsService.class);

    private AccountRepository accountRepository;

    @Autowired
    public SecurityUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Looking for user: {}", username);
        Account account = accountRepository.findByLoginIgnoringCase(username);
        if (account == null) {
            throw new UsernameNotFoundException(String.format("No user: '%s' found!", username));
        }
        log.debug("User found: {}. Role: {}", account.getLogin(), account.getUserRole());
        return new SecurityUserDetails(account);
    }
}
