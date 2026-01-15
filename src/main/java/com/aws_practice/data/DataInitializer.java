package com.aws_practice.data;

import com.aws_practice.models.Role;
import com.aws_practice.models.User;
import com.aws_practice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Component
@Transactional
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Initialize data");
        Set<String> defaultRoles =  Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExits(defaultRoles);
        createDefaultUserIfNotExists();
        createDefaultAdminIfNotExits();
    }

    private void createDefaultUserIfNotExists(){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i = 0; i < 5; i++){
            String defaultEmail = "user" + i + "@email.com";
            if(userRepository.existsByEmail(defaultEmail))
                continue;
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("user123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }

    private void createDefaultRoleIfNotExits(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);

    }

    private void createDefaultAdminIfNotExits(){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 0; i<2; i++){
            String defaultEmail = "admin"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("admin123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully.");
        }
    }


}
