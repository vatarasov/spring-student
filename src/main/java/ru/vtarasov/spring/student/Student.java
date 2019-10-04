package ru.vtarasov.spring.student;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author vtarasov
 * @since 21.09.2019
 */
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true, builderMethodName = "")
@Getter
@Entity
public class Student {
    private Student() {
        name = null;
        age = null;
    }

    @Id
    @GeneratedValue(generator = StringIdGenerator.GENERATOR)
    @GenericGenerator(name = StringIdGenerator.GENERATOR, strategy = StringIdGenerator.STRATEGY)
    private String id;

    @NotEmpty
    private final String name;

    @NotNull
    @Min(16)
    private final Integer age;
}
