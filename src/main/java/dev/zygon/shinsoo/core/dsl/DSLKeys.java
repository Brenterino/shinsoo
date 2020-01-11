/*
    Shinsoo: Java-Quarkus Back End for Aria
    Copyright (C) 2020  Brenterino <brent@zygon.dev>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package dev.zygon.shinsoo.core.dsl;

/**
 * Provides a list of keys to values of Domain Specific Language (DSL)
 * values contained within a {@link dev.zygon.shinsoo.dsl.DSLDictionary}.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
public class DSLKeys {

    public static final String USER_TABLE = "user.table";
    public static final String USER_USERNAME_COLUMN = "user.username.column";
    public static final String USER_MAPLE_ID_COLUMN = "user.maple_id.column";
    public static final String USER_PASSWORD_COLUMN = "user.password.column";
    public static final String USER_EMAIL_COLUMN = "user.email.column";
    public static final String USER_GM_LEVEL_COLUMN = "user.gm.level.column";

    public static final String PLAYER_TABLE = "player.table";
    public static final String PLAYER_RANK_COLUMN = "player.rank.column";
    public static final String PLAYER_NAME_COLUMN = "player.name.column";
    public static final String PLAYER_LEVEL_COLUMN = "player.level.column";
    public static final String PLAYER_EXP_COLUMN = "player.exp.column";
    public static final String PLAYER_FAME_COLUMN = "player.fame.column";
    public static final String PLAYER_JOB_COLUMN = "player.job.column";
    public static final String PLAYER_GUILD_ID_COLUMN = "player.guild_id.column";

    public static final String GUILD_TABLE = "guild.table";
    public static final String GUILD_ID_COLUMN = "guild.id.column";
    public static final String GUILD_NAME_COLUMN = "guild.name.column";

    public static final String SESSION_TABLE = "session.table";
    public static final String SESSION_NONCE_COLUMN = "session.nonce.column";
    public static final String SESSION_EXPIRE_COLUMN = "session.expire.column";
    public static final String SESSION_USERNAME_COLUMN = "session.username.column";
    public static final String SESSION_MAPLE_ID_COLUMN = "session.maple_id.column";
    public static final String SESSION_GM_LEVEL_COLUMN = "session.gm.level.column";

    public static final String POST_TABLE = "post.table";
    public static final String POST_ID_COLUMN = "post.id.column";
    public static final String POST_TYPE_COLUMN = "post.type.column";
    public static final String POST_VIEWS_COLUMN = "post.views.column";
    public static final String POST_TITLE_COLUMN = "post.title.column";
    public static final String POST_AUTHOR_COLUMN = "post.author.column";
    public static final String POST_CREATED_COLUMN = "post.created.column";
    public static final String POST_UPDATED_COLUMN = "post.updated.column";
    public static final String POST_CONTENT_COLUMN = "post.content.column";
}
