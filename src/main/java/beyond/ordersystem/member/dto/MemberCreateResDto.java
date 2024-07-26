package beyond.ordersystem.member.dto;

import beyond.ordersystem.common.domain.Address;
import beyond.ordersystem.member.domain.Member;
import beyond.ordersystem.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberCreateResDto {
    private String name;
    @NotEmpty(message = "email is essential")
    private String email;
    @NotEmpty(message = "password is essential")
//    @Size(min = 8, message = "password minimum length is 8")
    private String password;
    private Address address;

    public Member toEntity(String password){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(password)
                .address(this.address)
                .role(Role.USER)
                .build();
    }
}
