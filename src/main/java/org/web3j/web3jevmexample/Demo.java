/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.web3jevmexample;

import org.hyperledger.besu.ethereum.vm.OperationTracer;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.evm.Configuration;
import org.web3j.evm.PassthroughTracer;
import org.web3j.evm.EmbeddedWeb3jService;
import org.web3j.protocol.Web3j;
import org.web3j.quicksort.QuickSort;
import org.web3j.tx.gas.DefaultGasProvider;

import org.web3j.generator.*;


public class Demo {
    public static void main(String... args) throws Exception {
        Credentials credentials =
                WalletUtils.loadCredentials("Password123", "resources/demo-wallet.json");

        Configuration configuration = new Configuration(new Address(credentials.getAddress()), 10);

        OperationTracer operationTracer = new PassthroughTracer();

        Web3j web3j = Web3j.build(new EmbeddedWeb3jService(configuration,operationTracer));


        String quickSortContractAddress = QuickSort.deploy(web3j, credentials, new DefaultGasProvider()).send().getContractAddress();
        QuickSort deployedQuickSort = QuickSort.load(quickSortContractAddress, web3j, credentials,new DefaultGasProvider());

        for (int i = 1; i < 100; i++) {
            System.out.println(deployedQuickSort.sort(ArrayGenerator.generateNumericalArray(200, i)).send().getGasUsed());
        }
    }
}
