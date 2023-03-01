/*
Copyright IBM Corp. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0
*/
/*
Notice: This file has been modified for Hyperledger Fabric SDK Go usage.
Please review third_party pinning scripts and patches for more details.
*/

package discovery

import (
	"github.com/hyperledger/fabric-protos-go/discovery"
	"github.com/hyperledger/fabric-protos-go/peer"
	"github.com/hyperledger/fabric-sdk-go/internal/github.com/hyperledger/fabric/gossip/protoext"
	"github.com/pkg/errors"
	"google.golang.org/grpc"
)

var (
	// ErrNotFound defines an error that means that an element wasn't found
	ErrNotFound = errors.New("not found")
)

// Signer signs a message and returns the signature and nil,
// or nil and error on failure
type Signer func(msg []byte) ([]byte, error)

// Dialer connects to the server
type Dialer func() (*grpc.ClientConn, error)

// Response aggregates several responses from the discovery service
type Response interface {
	// ForChannel returns a ChannelResponse in the context of a given channel
	ForChannel(string) ChannelResponse

	// ForLocal returns a LocalResponse in the context of no channel
	ForLocal() LocalResponse
}

// ChannelResponse aggregates responses for a given channel
type ChannelResponse interface {
	// Config returns a response for a config query, or error if something went wrong
	Config() (*discovery.ConfigResult, error)

	// Peers returns a response for a peer membership query, or error if something went wrong
	Peers(invocationChain ...*peer.ChaincodeCall) ([]*Peer, error)
Endorsers(invocationChain InvocationChain, f Filter) (Endorsers, error)
}

type LocalResponse interface {
	// Peers returns a response for a local peer membership query, or error if something went wrong
	Peers() ([]*Peer, error)
}

type Endorsers []*Peer

type Peer struct {
	MSPID            string
	AliveMessage     *protoext.SignedGossipMessage
	StateInfoMessage *protoext.SignedGossipMessage
	Identity         []byte
}