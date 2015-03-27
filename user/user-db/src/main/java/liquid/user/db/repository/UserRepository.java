package liquid.user.db.repository;

import liquid.user.domain.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Tao Ma on 3/24/15.
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * @param specification
     * @return A list of users
     * A workaround for OneToOne N+1 problem.
     * <pre>
     * Specification<liquid.user.domain.User> specification = new Specification<liquid.user.domain.User>() {
     *     @Override
     *     public Predicate toPredicate(Root<liquid.user.domain.User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
     *         root.fetch("profile", JoinType.LEFT);
     *         return cb.equal(root.get(User_.username), root.get(User_.profile).get(UserProfile_.username));
     *     }
     * };
     * return userRepository.findAll(specification);
     * </pre>
     */
    @Deprecated
    List<User> findAll(Specification<User> specification);
}
