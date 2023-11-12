import Head from 'next/head';
import styles from './layout.module.css';
import utilStyles from '../styles/utils.module.css';
import Link from 'next/link';

export const siteTitle = 'Payment GW';
export const name = siteTitle;

export default function Layout({ children, home }) {
  return (
      <div className={styles.container}>
        <Head>
          <link rel="icon" href="/favicon.ico" />
          <meta name="og:title" content={siteTitle} />
          <meta name="twitter:card" content="summary_large_image" />
        </Head>
        <header className={styles.header}>
          {home ? (
              <>
                <h1 className={utilStyles.heading2Xl}>{name}</h1>
              </>
          ) : (
              <>
                <Link href="/">
                </Link>
                <h2 className={utilStyles.headingLg}>
                  <Link href="/" className={utilStyles.colorInherit}>
                    {name}
                  </Link>
                </h2>
              </>
          )}
        </header>
        <main>{children}</main>
        {!home && (
            <div className={styles.backToHome}>
              <Link href="/">← Back to home</Link>
            </div>
        )}
      </div>
  );
}
