package ssynx.gist;

import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;

/*!
 * @brief Main JaGist class
 */
public final class JaGist {

    /*!
     * @brief HTTP requests will contain basic authentication headers
     * @param username : username
     * @param passwd   : password
     */
    public static void setCredentials(final String username, final String passwd) {
        JaGistHttps.setBasicAuth(username,passwd);
    }

    /*!
     * @brief This method overload returns a timestamp in ISO 8601 by each date parameter
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return A timestamp in ISO 8601 format
     */
    public static String dateToTimestamp(int year, int month, int day, int hour, int minute, int second) {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02dZ",
                year,
                month,
                day,
                hour,
                minute,
                second);
    }

    /*!
     * @brief This method overload returns a timestamp in ISO 8601 by a Calendar object
     * @param c : Calendar object
     * @return A timestamp in ISO 8601 format
     */
    public static String dateToTimestamp(Calendar c) {
        return String.format("%04d-%02d-%02dT%02d:%02d:%02dZ",
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND));
    }

    /*!
     * @brief Inner static class which contains Gist-information fetching methods
     */
    public static class GetGist {

        /*!
         * @brief get me EEVERYTHINGGGG
         * @return public gists
         */
        public static Gist[] pub()
                throws JaGistException {
            final JSONArray gistsArray;
            final Vector<Gist> gists = new Vector<>();
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get();
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr != null) {
                gistsArray = new JSONArray(jsonStr);
                for (int i = 0; i < gistsArray.length(); i++)
                    gists.add(new Gist(gistsArray.get(i).toString()));
            }

            return gists.toArray(new Gist[gists.size()]);
        }

        /*!
         * @brief check JaGist.dateToTimestamp() method
         * @param timestamp : ISO-8601 timetamp
         * @return Gists from that date to now
         */
        public static Gist[] pub(final String timestamp)
                throws JaGistException {
            final JSONArray gistsArray;
            final Vector<Gist> gists = new Vector<>();
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get("/gists","?since="+timestamp);
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr != null) {
                gistsArray = new JSONArray(jsonStr);
                for (int i = 0; i < gistsArray.length(); i++)
                    gists.add(new Gist(gistsArray.get(i).toString()));
            }

            return gists.toArray(new Gist[gists.size()]);
        }

        /*!
         * @brief get every gist from 'user'
         * @param user : username
         * @return user's gists
         */
        public static Gist[] user(final String user)
                throws JaGistException {
            final JSONArray gistsArray;
            final Vector<Gist> gists = new Vector<>();
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get("/users/"+user+"/gists","");
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr != null) {
                gistsArray = new JSONArray(jsonStr);
                for (int i = 0; i < gistsArray.length(); i++)
                    gists.add(new Gist(gistsArray.get(i).toString()));
            }

            return gists.toArray(new Gist[gists.size()]);
        }

        /*!
         * @brief get YOUR starred gists (check setCredentials() method)
         * [REQUIRES AUTHENTICATION]
         * @return your starred gists
         */
        public static Gist[] starred()
                throws JaGistException {
            final JSONArray gistsArray;
            final Vector<Gist> gists = new Vector<>();
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get("/gists/starred","");
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr != null) {
                gistsArray = new JSONArray(jsonStr);
                for (int i = 0; i < gistsArray.length(); i++)
                    gists.add(new Gist(gistsArray.get(i).toString()));
            }

            return gists.toArray(new Gist[gists.size()]);
        }

        /*!
         * @brief retrieves single gist by its id
         * @param id : gist's id
         * @return requested gist ( WARNING: MAY BE NULL! )
         */
        @Nullable
        public static Gist single(final String id)
                throws JaGistException {
            final JSONObject obj;
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get("/gists/"+id,"");
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr == null)
                return null;

            return new Gist(jsonStr);
        }

        /*!
         * @brief retrieves single gist by its id, with specific revision
         * @param id : gist's id
         * @param commitSha : gist specific revision commit SHA
         * @return requested gist by commit ( WARNING: MAY BE NULL! )
         */
        @Nullable
        public static Gist single(final String id, final String commitSha)
                throws JaGistException {
            final JSONObject obj;
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get("/gists/"+id+"/"+commitSha,"");
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr == null)
                return null;

            return new Gist(jsonStr);
        }

        /*!
         * @brief Retrieves all commits for Gist's ID
         * @param id : gist ID
         * @return all of its commits
         */
        public static GistHistory[] singleCommits(final String id)
                throws JaGistException {
            final JSONArray gistsArray;
            final Vector<GistHistory> gists = new Vector<>();
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get("/gists/"+id+"/commits","");
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr != null) {
                gistsArray = new JSONArray(jsonStr);
                for (int i = 0; i < gistsArray.length(); i++)
                    gists.add(new GistHistory(new JSONObject(gistsArray.get(i).toString())));
            }

            return gists.toArray(new GistHistory[gists.size()]);
        }

        /*!
         * @brief checks if you starred a gist by its id (check setCredentials() method)
         * [REQUIRES AUTHENTICATION]
         * @return did you starred that gist?
         */
        public static boolean isStarred(final String id)
                throws JaGistException {
            try {
                JaGistHttps.get("/gists/" + id + "/star", "");
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code == 404)
                    return false;

                throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                        code);
            }

            return true;
        }

        /*!
         * @brief specific-gist forks
         * @param id : gist's id
         * @return Gist forks
         */
        public static Gist[] forks(final String id)
                throws JaGistException {
            final JSONArray gistsArray;
            final Vector<Gist> gists = new Vector<>();
            String jsonStr = null;

            try {
                jsonStr = JaGistHttps.get("/gists/"+id+"/forks","");
            } catch(IOException ioe) {
                int code = JaGistHttps.getLastCode();
                if(code != 404)
                    throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                            code);
            }

            if(jsonStr != null) {
                gistsArray = new JSONArray(jsonStr);
                for (int i = 0; i < gistsArray.length(); i++)
                    gists.add(new Gist(gistsArray.get(i).toString()));
            }

            return gists.toArray(new Gist[gists.size()]);
        }
    }

    /*!
     * @brief Inner static class which contains Gists information changing methods
     */
    public static class PerformGist {
        @Nullable
        public static Gist create(final NewGist gist)
                throws JaGistException {
            String newGist;
            try {
                newGist = JaGistHttps.post("",gist.toString());
            } catch(IOException ioe) {
                throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                        JaGistHttps.getLastCode());
            }

            if(newGist == null) //this code may never be reached...
                return null;

            return new Gist(newGist);
        }

        @Nullable
        public static Gist edit(final EditGist gist,final String id)
                throws JaGistException {
            String newGist;
            try {
                newGist = JaGistHttps.patch("/"+id,gist.toString());
            } catch(IOException ioe) {
                throw new JaGistException(JaGistHttps.getLastErrorMessage(),
                        JaGistHttps.getLastCode());
            }

            if(newGist == null) //this code may never be reached
                return null;

            return new Gist(newGist);
        }

        public static boolean star(final String id) {
            return false;
        }

        public static boolean unstar(final String id) {
            return false;
        }

        public static Gist fork(final String id) {
            return null;
        }

        public static boolean delete(final String id) {
            return false;
        }
    }

}
