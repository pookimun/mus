package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Notice extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null허용여부
    private String n_title;

    @Column(length = 2000, nullable = false)
    private String n_content;

    @Column(length = 50, nullable = false)
    private String n_writer;

    public void change(String title, String content){
        this.n_title = title;
        this.n_content = content;
    }

    @OneToMany(mappedBy = "notice",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<NoticeImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){

        NoticeImage noticeImage = NoticeImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .notice(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(noticeImage);
    }

    public void clearImages() {
        imageSet.forEach(noticeImage -> noticeImage.changeNotice(null));
        this.imageSet.clear();
    }

}
