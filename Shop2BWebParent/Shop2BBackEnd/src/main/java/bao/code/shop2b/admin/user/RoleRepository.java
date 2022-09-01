package bao.code.shop2b.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bao.code.shop2b.common.entity.Role;
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
