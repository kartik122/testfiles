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
	"math/rand"
	"sort"
	"time"

	"github.com/hyperledger/fabric-sdk-go/internal/github.com/hyperledger/fabric/gossip/protoext"
)

type Filter interface {
	Filter(endorsers Endorsers) Endorsers
}
type Priority int

var (
	// PrioritiesByHeight selects peers by descending height
	PrioritiesByHeight = &byHeight{}
	// NoExclusion accepts all peers and rejects no peers
	NoExclusion = selectionFunc(noExclusion)
	// NoPriorities is indifferent to how it selects peers
	NoPriorities = &noPriorities{}
)

type noPriorities struct{}

func (nc noPriorities) Compare(_ Peer, _ Peer) Priority {
	return 0
}

type byHeight struct{}

func (*byHeight) Compare(left Peer, right Peer) Priority {
	leftHeight := left.StateInfoMessage.GetStateInfo().Properties.LedgerHeight
	rightHeight := right.StateInfoMessage.GetStateInfo().Properties.LedgerHeight

	if leftHeight > rightHeight {
		return 1
	}
	if rightHeight > leftHeight {
		return -1
	}
	return 0
}

func noExclusion(_ Peer) bool {
	return false
}

// Sort sorts the endorsers according to the given PrioritySelector
func (endorsers Endorsers) Sort(ps PrioritySelector) Endorsers {
	sort.Sort(&endorserSort{
		Endorsers:        endorsers,
		PrioritySelector: ps,
	})
	return endorsers
}

func (es *endorserSort) Len() int {
	return len(es.Endorsers)
}

func (es *endorserSort) Less(i, j int) bool {
	e1 := es.Endorsers[i]
	e2 := es.Endorsers[j]
	less := es.Compare(*e1, *e2)
	return less > Priority(0)
}

func (es *endorserSort) Swap(i, j int) {
	es.Endorsers[i], es.Endorsers[j] = es.Endorsers[j], es.Endorsers[i]
}