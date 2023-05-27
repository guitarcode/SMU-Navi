package smu.poodle.smnavi.map.domain.path;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Deprecated
public class RouteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_info_id")
    Integer id;

    Boolean isMain;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Edge edge;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    FullPath fullPath;
}
