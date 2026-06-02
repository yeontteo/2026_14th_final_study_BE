package org.likelion.domain.member.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 500)
    private String refreshToken;

    protected Member() {}

    private Member(Long id, String name, String email, String password, String refreshToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
    }

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRefreshToken() { return refreshToken; }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static class MemberBuilder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private String refreshToken;

        public MemberBuilder id(Long id) { this.id = id; return this; }
        public MemberBuilder name(String name) { this.name = name; return this; }
        public MemberBuilder email(String email) { this.email = email; return this; }
        public MemberBuilder password(String password) { this.password = password; return this; }
        public MemberBuilder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }

        public Member build() {
            return new Member(id, name, email, password, refreshToken);
        }
    }
}