package org.poc.shop.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.poc.shop.dto.request.ClientAddressRequest;
import org.poc.shop.dto.request.ClientRequest;
import org.poc.shop.dto.response.ClientResponse;
import org.poc.shop.entity.Client;
import org.poc.shop.entity.ClientAddress;
import org.poc.shop.repository.ClientRepository;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class ClientService {

    @Inject
    private ClientRepository clientRepository;

    public List<ClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll().list();
        List<ClientResponse> clientResponses = new ArrayList<>() ;
        for (Client client : clients) {
            ClientResponse clientResponse = new ClientResponse();
            clientResponse.setId(client.getId());
            clientResponse.setName(client.getName());
            clientResponse.setEmail(client.getEmail());
            clientResponse.setPhone(client.getPhone());
            clientResponses.add(clientResponse);
        }
        return clientResponses;
    }

    public Client createClient(ClientRequest clientRequest) {

            Client client = new Client();
            client.setName(clientRequest.getName());
            client.setPhone(clientRequest.getPhone());
            client.setEmail(clientRequest.getEmail());
            List<ClientAddress> addresses = new ArrayList<>();

            for (ClientAddressRequest addressRequest : clientRequest.getAddresses()) {
                ClientAddress address = new ClientAddress();
                address.setName(addressRequest.getName());
                address.setLongitude(addressRequest.getLongitude());
                address.setLatitude(addressRequest.getLatitude());
                address.setClient(client);
                addresses.add(address);
            }
            client.setAddresses(addresses);
            clientRepository.persist(client);

            return  client;


    }
}
