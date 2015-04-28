/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.hbase.adapters;

import com.google.bigtable.v1.ReadRowsRequest;
import com.google.protobuf.ByteString;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;

/**
 * A Get adapter that transform the Get into a ReadRowsRequest using the proto-based
 * filter language.
 */
public class GetAdapter implements OperationAdapter<Get, ReadRowsRequest.Builder> {

  protected final ScanAdapter scanAdapter;
  public GetAdapter(ScanAdapter scanAdapter) {
    this.scanAdapter = scanAdapter;
  }

  @Override
  public ReadRowsRequest.Builder adapt(Get operation) {
    Scan operationAsScan = new Scan(operation);
    scanAdapter.throwIfUnsupportedScan(operationAsScan);
    return ReadRowsRequest.newBuilder()
        .setFilter(scanAdapter.buildFilter(operationAsScan))
        .setRowKey(ByteString.copyFrom(operation.getRow()));
  }
}
