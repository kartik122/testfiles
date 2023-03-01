/*
Copyright IBM Corp. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
/*
Notice: This file has been modified for Hyperledger Fabric SDK Go usage.
Please review third_party pinning scripts and patches for more details.
*/

package api

import (
	"time"

	"github.com/cloudflare/cfssl/csr"
	"github.com/hyperledger/fabric-sdk-go/internal/github.com/hyperledger/fabric-ca/sdkinternal/pkg/util"
)

// RegistrationRequest for a new identity
type RegistrationRequest struct {
	Name string `json:"id" help:"Unique name of the identity"`
	Type string `json:"type" def:"client" help:"Type of identity being registered (e.g. 'peer, app, user')"`
	Secret string `json:"secret,omitempty" mask:"password" help:"The enrollment secret for the identity being registered"`
	MaxEnrollments int `json:"max_enrollments,omitempty" help:"The maximum number of times the secret can be reused to enroll (default CA's Max Enrollment)"`
	Affiliation string `json:"affiliation" help:"The identity's affiliation"`
	Attributes []Attribute `json:"attrs,omitempty"`
	CAName string `json:"caname,omitempty" skip:"true"`
}

func (rr *RegistrationRequest) String() string {
	return util.StructToString(rr)
}

type RegistrationResponse struct {
	Secret string `json:"secret"`
}

type EnrollmentRequest struct {
	Name string `json:"name" skip:"true"`
	Secret string `json:"secret,omitempty" skip:"true" mask:"password"`
	CAName string `json:"caname,omitempty" skip:"true"`
	AttrReqs []*AttributeRequest `json:"attr_reqs,omitempty"`
	Profile string `json:"profile,omitempty" help:"Name of the signing profile to use in issuing the certificate"`
	Label string `json:"label,omitempty" help:"Label to use in HSM operations"`
	CSR *CSRInfo `json:"csr,omitempty" skip:"true"` // Skipping this because we pull the CSR from the CSR flags
	Type string `def:"x509" help:"The type of enrollment request: 'x509' or 'idemix'"`
}