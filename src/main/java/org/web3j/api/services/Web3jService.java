package org.web3j.api.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.hyperledger.besu.ethereum.vm.OperationTracer;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.evm.Configuration;
import org.web3j.evm.PassthroughTracer;
import org.web3j.evm.EmbeddedWeb3jService;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.regreeter.Regreeter;

public class Web3jService {

    private Credentials credentials;
    private Configuration configuration;
    private OperationTracer operationTracer;
    private Web3j web3j;

    public Web3jService() {
        try {
            // credentials doit etre définit en 1er car configuration utilise credentials
            this.credentials = this.loadCredential("Password123", "resources/demo-wallet.json");
            this.configuration = this.loadConfiguration();
            this.operationTracer = this.loadOperationTracer();
            this.web3j = Web3j.build(new EmbeddedWeb3jService(this.configuration, this.operationTracer));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void process(Class clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        Object ob = clazz.getDeclaredConstructor().newInstance(); // ne fonctionne pas
        
    }

    private OperationTracer loadOperationTracer(){
        return new PassthroughTracer();
    }

    private Configuration loadConfiguration(){
        return new Configuration(new Address(this.credentials.getAddress()), 10);
    }

    private Credentials loadCredential(String s, String ressourceName) throws IOException, CipherException {
        return WalletUtils.loadCredentials("Password123", "resources/demo-wallet.json");
    }

}
