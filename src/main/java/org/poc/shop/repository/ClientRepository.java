package org.poc.shop.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.poc.shop.entity.Client;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {

}
