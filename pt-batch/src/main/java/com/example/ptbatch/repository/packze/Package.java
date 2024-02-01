package com.example.ptbatch.repository.packze;

import com.example.ptbatch.repository.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@Table(name = "package")
@Entity
@ToString
public class Package extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageSeq;

    @Setter
    private String packageName;

    @Setter
    private Integer count;

    @Setter
    private Integer period;

    private Package(String packageName, Integer count, Integer period) {
        this.packageName = packageName;
        this.count = count;
        this.period = period;
    }

    protected Package() {

    }

    public static Package of(String packageName, Integer count, Integer period) {
        return new Package(packageName, count, period);
    }

}
