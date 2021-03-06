package com.idetix.hostbackend.Service.Blockchain;

import com.idetix.hostbackend.Entity.Exceptions.BlockChainComunicationException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class BlockchainServiceImpl implements BlockchainService {
    final Web3j web3;
    final Credentials credentials;
    final String eventContractAddress;

    @SneakyThrows
    @Autowired
    public BlockchainServiceImpl(
            @Value("${BlockchainPath}") String blockchainPath,
            @Value("${BlockchainPrivatKey}") String privateKey,
            @Value("${EventContractAddress}") String eventContractAddress) {
        web3 = Web3j.build(new HttpService(blockchainPath));
        credentials = Credentials.create(privateKey);
        this.eventContractAddress = eventContractAddress;
    }

    public int getGeneralTicketAmountForAddress(String ethAddress) throws BlockChainComunicationException {
        Function function = new Function("totalTickets", // Function name
                Collections.singletonList(new Address(ethAddress)), // input parameters
                Collections.singletonList(new TypeReference<Uint256>() {
                })); // return parameters

        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response;
        try {
            response = web3.ethCall(
                    Transaction.createEthCallTransaction(credentials.getAddress(), eventContractAddress, encodedFunction),
                    DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new BlockChainComunicationException(e.getMessage());
        }

        List<Type> someTypes = FunctionReturnDecoder.decode(
                response.getValue(), function.getOutputParameters());
        Uint256 uint256 = (Uint256) someTypes.get(0);
        return uint256.getValue().intValue();

    }

    @Override
    public int getTicketAmountForType(String ethAddress, List<String> ticketTypes) throws BlockChainComunicationException {
        int amountOfTickets = 0;
        for (String ticketType : ticketTypes) {
            Function function = new Function("tickets", // Function name
                    Arrays.asList(new Uint256(Long.decode(ticketType)), new Address(ethAddress)), // input parameters
                    Collections.singletonList(new TypeReference<Uint256>() {
                    })); // return parameters

            String encodedFunction = FunctionEncoder.encode(function);
            EthCall response;
            try {
                response = web3.ethCall(
                        Transaction.createEthCallTransaction(credentials.getAddress(), eventContractAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST).sendAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new BlockChainComunicationException(e.getMessage());
            }

            List<Type> someTypes = FunctionReturnDecoder.decode(
                    response.getValue(), function.getOutputParameters());
            Uint256 uint256 = (Uint256) someTypes.get(0);
            amountOfTickets = amountOfTickets + uint256.getValue().intValue();
        }
        return amountOfTickets;
    }

}
