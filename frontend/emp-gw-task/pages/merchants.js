import Link from 'next/link';
import Layout from "../components/layout";

export default function Merchants({merchantsData}) {
  return (
      <Layout>
        <h1>Merchants</h1>
        <MerchantTable merchants={merchantsData}/>
      </Layout>
  );
}

export async function getStaticProps() {
  // Get external data from the file system, API, DB, etc.
  const merchantsData =

      [
        {
          id: 1,
          merchantName: "merchant 1",
          description: "description 1",
          email: "a@b.c",
          active: true,
          totalTransactionAmount: 0.0
        },
        {
          id: 1,
          merchantName: "merchant 1",
          description: "description 1",
          email: "a@b.c",
          active: true,
          totalTransactionAmount: 0.0
        }];

  // The value of the `props` key will be
  //  passed to the `Home` component
  return {
    props: {merchantsData}
  }

}

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

function MerchantRow({merchant}) {

  return (<tr>
    <td>{merchant.id}</td>
    <td>{merchant.merchantName}</td>
    <td>{merchant.description}</td>
    <td>{merchant.email}</td>
    <td>{merchant.active}</td>
    <td>{merchant.totalTransactionAmount}</td>
  </tr>);
}


