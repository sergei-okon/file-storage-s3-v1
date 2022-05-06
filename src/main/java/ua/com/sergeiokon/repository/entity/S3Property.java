package ua.com.sergeiokon.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "property")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class S3Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prop_key", nullable = false, unique = true)
    private String key;

    @Column(name = "prop_value", nullable = false)
    private String value;
}
