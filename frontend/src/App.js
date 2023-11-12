import {Container} from "react-bootstrap";
import * as PropTypes from "prop-types";
import {Component} from "react";

class MerchantRow extends Component {
  render() {
    return (<tr>
      <td>{this.props.merchant.id}</td>
      <td>{this.props.merchant.merchantName}</td>
      <td>{this.props.merchant.description}</td>
      <td>{this.props.merchant.email}</td>
      <td>{this.props.merchant.active}</td>
      <td>{this.props.merchant.totalTransactionAmount}</td>
    </tr>);
  }
}

MerchantRow.propTypes = {
  id: PropTypes.any,
  merchantName: PropTypes.any,
  description: PropTypes.any,
  email: PropTypes.any,
  active: PropTypes.any,
  totalTransactionAmount: PropTypes.any
};

function MerchantTable({merchants}) {
  const rows = [];

  merchants.forEach((merchant) => {
    rows.push(
        <MerchantRow
            merchant={merchant}
        />
    );
  });

  return (
      <table>
        <thead>
        <tr>
          <th>Id</th>
          <th>Name</th>
          <th>Description</th>
          <th>e-mail</th>
          <th>Active</th>
          <th>Total Amount</th>
        </tr>
        </thead>
        <tbody>{rows}</tbody>
      </table>

  );
}

export default function App() {
  return (
      <Container className="p-3">
        <h1>Merchants</h1>
        <div>
          <MerchantTable merchants={MERCHANTS}/>
        </div>
      </Container>
  );
}

const MERCHANTS = [
  {
    id: 1,
    merchantName: "merchant 1",
    description: "description 1",
    email: "a@b.c",
    active: true,
    totalTransactionAmount: 0.0
  }
];